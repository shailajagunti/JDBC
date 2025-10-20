import type { Product } from "@prisma/client";

type ProductLike = Pick<Product, "id" | "name" | "description" | "tags" | "categoryId">;

function tokenize(text: string): string[] {
  return text
    .toLowerCase()
    .replace(/[^a-z0-9\s,]+/g, " ")
    .split(/[\s,]+/)
    .filter(Boolean);
}

export function buildTfIdfVectors(products: ProductLike[]): {
  vocabulary: Map<string, number>;
  vectors: Map<number, Map<string, number>>;
} {
  const docsTokens: Map<number, string[]> = new Map();
  const df: Map<string, number> = new Map();

  for (const p of products) {
    const tokens = tokenize(`${p.name} ${p.description} ${p.tags ?? ""}`);
    docsTokens.set(p.id, tokens);
    const uniqueTokens = new Set(tokens);
    for (const t of uniqueTokens) {
      df.set(t, (df.get(t) ?? 0) + 1);
    }
  }

  const vocabulary = new Map<string, number>();
  let index = 0;
  for (const term of df.keys()) {
    vocabulary.set(term, index++);
  }

  const N = products.length;
  const vectors: Map<number, Map<string, number>> = new Map();

  for (const [id, tokens] of docsTokens.entries()) {
    const tf: Map<string, number> = new Map();
    for (const t of tokens) tf.set(t, (tf.get(t) ?? 0) + 1);
    const vec: Map<string, number> = new Map();
    for (const [term, freq] of tf.entries()) {
      const idf = Math.log((N + 1) / ((df.get(term) ?? 0) + 1)) + 1; // smoothed idf
      vec.set(term, (freq / tokens.length) * idf);
    }
    vectors.set(id, vec);
  }

  return { vocabulary, vectors };
}

function cosineSimilarity(a: Map<string, number>, b: Map<string, number>): number {
  let dot = 0;
  let aNorm = 0;
  let bNorm = 0;
  for (const [term, aVal] of a.entries()) {
    aNorm += aVal * aVal;
    const bVal = b.get(term);
    if (bVal) dot += aVal * bVal;
  }
  for (const val of b.values()) bNorm += val * val;
  if (aNorm === 0 || bNorm === 0) return 0;
  return dot / (Math.sqrt(aNorm) * Math.sqrt(bNorm));
}

export function recommendSimilar(
  products: ProductLike[],
  targetId: number,
  opts?: { limit?: number; sameCategoryBoost?: number }
): ProductLike[] {
  const limit = opts?.limit ?? 6;
  const sameCategoryBoost = opts?.sameCategoryBoost ?? 0.1;
  const { vectors } = buildTfIdfVectors(products);
  const target = vectors.get(targetId);
  if (!target) return [];
  const targetProduct = products.find((p) => p.id === targetId);
  if (!targetProduct) return [];

  const scored = products
    .filter((p) => p.id !== targetId)
    .map((p) => {
      const sim = cosineSimilarity(target, vectors.get(p.id) ?? new Map());
      const categoryBonus = p.categoryId === targetProduct.categoryId ? sameCategoryBoost : 0;
      return { p, score: sim + categoryBonus };
    })
    .sort((a, b) => b.score - a.score)
    .slice(0, limit)
    .map((x) => x.p);

  return scored;
}
