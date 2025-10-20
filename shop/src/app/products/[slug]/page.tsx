import Link from "next/link";
import { prisma } from "@/lib/prisma";
import { AddToCart } from "@/components/AddToCart";

async function getData(slug: string) {
  const product = await prisma.product.findUnique({ where: { slug } });
  if (!product) return null;
  const res = await fetch(`${process.env.NEXT_PUBLIC_BASE_URL || "http://localhost:3000"}/api/recommendations?productId=${product.id}`, {
    cache: "no-store",
  });
  const data = (await res.json()) as { recommendations: typeof product[] };
  return { product, recs: data.recommendations };
}

export default async function ProductDetail({ params }: { params: { slug: string } }) {
  const { slug } = params;
  const data = await getData(slug);
  if (!data) {
    return (
      <div className="max-w-3xl mx-auto p-6">
        <p>Product not found.</p>
        <Link className="text-blue-600" href="/products">Back to products</Link>
      </div>
    );
  }

  const { product, recs } = data;

  return (
    <div className="max-w-5xl mx-auto p-6">
      <Link className="text-blue-600" href="/products">Back</Link>
      <div className="mt-4 grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="md:col-span-2">
          <h1 className="text-2xl font-semibold">{product.name}</h1>
          <p className="mt-2 opacity-80">{product.description}</p>
          <div className="mt-4 text-lg font-medium">${(product.priceCents / 100).toFixed(2)}</div>
          <AddToCart product={{ id: product.id, name: product.name, slug: product.slug, priceCents: product.priceCents }} />
        </div>
        <div className="md:col-span-1">
          <div className="border rounded p-4">
            <div className="font-medium mb-2">You may also like</div>
            <ul className="space-y-2">
              {recs.map((r) => (
                <li key={r.id}>
                  <Link className="text-blue-600" href={`/products/${r.slug}`}>{r.name}</Link>
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
}
