import { NextRequest, NextResponse } from "next/server";
import { prisma } from "@/lib/prisma";
import { recommendSimilar } from "@/lib/recommend";

export async function GET(req: NextRequest) {
  const { searchParams } = new URL(req.url);
  const productIdParam = searchParams.get("productId");
  if (!productIdParam) {
    return NextResponse.json({ error: "Missing productId" }, { status: 400 });
  }
  const productId = Number(productIdParam);
  if (Number.isNaN(productId)) {
    return NextResponse.json({ error: "Invalid productId" }, { status: 400 });
  }

  const products = await prisma.product.findMany();
  const recs = recommendSimilar(products, productId, { limit: 6 });

  return NextResponse.json({ recommendations: recs });
}
