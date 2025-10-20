import Link from "next/link";

export default function Home() {
  return (
    <div className="max-w-3xl mx-auto p-6">
      <h1 className="text-2xl font-semibold">Welcome to Shop</h1>
      <p className="mt-2 opacity-80">A minimal e-commerce demo with AI recommendations.</p>
      <Link className="mt-4 inline-block text-blue-600" href="/products">
        Browse products →
      </Link>
    </div>
  );
}
