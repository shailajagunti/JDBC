const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

async function main() {
  await prisma.orderItem.deleteMany();
  await prisma.order.deleteMany();
  await prisma.product.deleteMany();
  await prisma.category.deleteMany();

  const electronics = await prisma.category.create({
    data: { name: "Electronics", slug: "electronics" },
  });
  const apparel = await prisma.category.create({
    data: { name: "Apparel", slug: "apparel" },
  });
  const home = await prisma.category.create({ data: { name: "Home", slug: "home" } });

  await prisma.product.createMany({
    data: [
      {
        name: "Wireless Headphones",
        slug: "wireless-headphones",
        description:
          "Comfortable over-ear headphones with noise cancellation and 30h battery.",
        priceCents: 12999,
        imageUrl: "/next.svg",
        tags: "audio,wireless,bluetooth,music",
        categoryId: electronics.id,
      },
      {
        name: "Smartwatch Series X",
        slug: "smartwatch-series-x",
        description: "Fitness tracking, notifications, and GPS in a sleek design.",
        priceCents: 19999,
        imageUrl: "/next.svg",
        tags: "wearable,fitness,health,bluetooth",
        categoryId: electronics.id,
      },
      {
        name: "Graphic T-Shirt",
        slug: "graphic-tshirt",
        description: "Soft cotton tee with minimalist design.",
        priceCents: 2499,
        imageUrl: "/next.svg",
        tags: "clothing,casual,cotton",
        categoryId: apparel.id,
      },
      {
        name: "Ceramic Mug",
        slug: "ceramic-mug",
        description: "12oz ceramic mug, dishwasher-safe.",
        priceCents: 1299,
        imageUrl: "/next.svg",
        tags: "kitchen,coffee,tea",
        categoryId: home.id,
      },
      {
        name: "LED Desk Lamp",
        slug: "led-desk-lamp",
        description: "Adjustable brightness and color temperature for focused work.",
        priceCents: 3999,
        imageUrl: "/next.svg",
        tags: "lighting,home-office,usb",
        categoryId: home.id,
      },
    ],
  });
}

main()
  .then(async () => {
    await prisma.$disconnect();
  })
  .catch(async (e) => {
    console.error(e);
    await prisma.$disconnect();
    process.exit(1);
  });
