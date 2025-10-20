import Link from "next/link";
import { prisma } from "@/lib/prisma";

export default async function ProductsPage() {
  const products = await prisma.product.findMany({ orderBy: { id: "asc" } });
  return (
    <div className="max-w-5xl mx-auto p-6">
      <h1 className="text-2xl font-semibold mb-4">Products</h1>
      <ul className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {products.map((p) => (
          <li key={p.id} className="border rounded p-4">
            <div className="font-medium">{p.name}</div>
            <div className="text-sm opacity-70 line-clamp-2">{p.description}</div>
            <div className="mt-2 text-sm">${(p.priceCents / 100).toFixed(2)}</div>
            <Link className="mt-3 inline-block text-blue-600" href={`/products/${p.slug}`}>
              View
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
