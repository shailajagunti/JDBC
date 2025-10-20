"use client";

import { useEffect, useState } from "react";
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

export default function CheckoutPage() {
  const [cart, setCart] = useState<CartItem[]>([]);
  const [email, setEmail] = useState("");
  const [complete, setComplete] = useState(false);

  useEffect(() => setCart(loadCart()), []);

  const placeOrder = async () => {
    // Mock checkout
    await new Promise((r) => setTimeout(r, 500));
    localStorage.removeItem("cart");
    setComplete(true);
  };

  if (complete)
    return (
      <div className="max-w-2xl mx-auto p-6">
        <h1 className="text-2xl font-semibold">Thank you!</h1>
        <p className="mt-2">Your order has been placed.</p>
        <Link className="mt-4 inline-block text-blue-600" href="/products">
          Continue shopping
        </Link>
      </div>
    );

  return (
    <div className="max-w-2xl mx-auto p-6">
      <h1 className="text-2xl font-semibold">Checkout</h1>
      <div className="mt-4">
        <label className="block text-sm">Email</label>
        <input
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="you@example.com"
          className="mt-1 w-full border rounded px-3 py-2"
        />
      </div>
      <button
        onClick={placeOrder}
        className="mt-6 px-4 py-2 bg-black text-white rounded disabled:opacity-50"
        disabled={cart.length === 0 || !email}
      >
        Place order
      </button>
    </div>
  );
}
