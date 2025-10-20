"use client";

import { useState } from "react";
import Link from "next/link";

export type AddToCartProduct = {
  id: number;
  name: string;
  slug: string;
  priceCents: number;
};

export function AddToCart({ product }: { product: AddToCartProduct }) {
  const [qty, setQty] = useState(1);
  const addToCart = () => {
    const raw = localStorage.getItem("cart");
    const cart = raw ? (JSON.parse(raw) as any[]) : [];
    const existing = cart.find((i) => i.id === product.id);
    if (existing) existing.quantity += qty;
    else cart.push({ ...product, quantity: qty });
    localStorage.setItem("cart", JSON.stringify(cart));
  };

  return (
    <div className="mt-4 flex items-center gap-2">
      <input
        type="number"
        min={1}
        value={qty}
        onChange={(e) => setQty(Math.max(1, Number(e.target.value)))}
        className="w-20 border rounded px-2 py-1"
      />
      <button onClick={addToCart} className="px-4 py-2 bg-black text-white rounded">
        Add to cart
      </button>
      <Link className="text-blue-600" href="/cart">
        View cart
      </Link>
    </div>
  );
}
