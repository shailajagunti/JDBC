"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";

type CartItem = {
  id: number;
  name: string;
  priceCents: number;
  quantity: number;
  slug: string;
};

function loadCart(): CartItem[] {
  if (typeof window === "undefined") return [];
  try {
    const raw = localStorage.getItem("cart");
    return raw ? (JSON.parse(raw) as CartItem[]) : [];
  } catch {
    return [];
  }
}

function saveCart(cart: CartItem[]) {
  localStorage.setItem("cart", JSON.stringify(cart));
}

export default function CartPage() {
  const [cart, setCart] = useState<CartItem[]>([]);
  useEffect(() => setCart(loadCart()), []);

  const totalCents = useMemo(
    () => cart.reduce((sum, i) => sum + i.priceCents * i.quantity, 0),
    [cart]
  );

  const remove = (id: number) => {
    const next = cart.filter((i) => i.id !== id);
    setCart(next);
    saveCart(next);
  };

  const updateQty = (id: number, qty: number) => {
    const next = cart.map((i) => (i.id === id ? { ...i, quantity: qty } : i));
    setCart(next);
    saveCart(next);
  };

  return (
    <div className="max-w-3xl mx-auto p-6">
      <h1 className="text-2xl font-semibold">Your Cart</h1>
      {cart.length === 0 ? (
        <p className="mt-4">
          Cart is empty. <Link className="text-blue-600" href="/products">Browse products</Link>
        </p>
      ) : (
        <>
          <ul className="mt-4 space-y-4">
            {cart.map((i) => (
              <li key={i.id} className="flex items-center justify-between border rounded p-3">
                <div className="flex-1">
                  <Link className="text-blue-600" href={`/products/${i.slug}`}>{i.name}</Link>
                  <div className="text-sm opacity-70">${(i.priceCents / 100).toFixed(2)}</div>
                </div>
                <div className="flex items-center gap-2">
                  <input
                    type="number"
                    min={1}
                    value={i.quantity}
                    onChange={(e) => updateQty(i.id, Math.max(1, Number(e.target.value)))}
                    className="w-16 border rounded px-2 py-1"
                  />
                  <button className="text-red-600" onClick={() => remove(i.id)}>Remove</button>
                </div>
              </li>
            ))}
          </ul>
          <div className="mt-6 flex items-center justify-between">
            <div className="text-lg font-medium">Total: ${(totalCents / 100).toFixed(2)}</div>
            <Link href="/checkout" className="px-4 py-2 bg-black text-white rounded">
              Checkout
            </Link>
          </div>
        </>
      )}
    </div>
  );
}
