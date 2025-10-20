Minimal e-commerce demo with AI recommendations (TF-IDF + cosine similarity), built on Next.js, Tailwind, and Prisma (SQLite).

### Getting started
- Install dependencies: `npm install`
- Setup DB: `npm run prisma:migrate && npm run prisma:seed`
- Dev server: `npm run dev` then open `http://localhost:3000`

### Key routes
- `/products`: Product listing
- `/products/[slug]`: Product detail with recommendations
- `/api/recommendations?productId=ID`: Recommendations API
- `/cart` and `/checkout`: LocalStorage cart and mock checkout

### Tech
- Next.js App Router, Tailwind CSS 4
- Prisma with SQLite; schema in `prisma/schema.prisma`
- Recommender: `src/lib/recommend.ts` (TF-IDF + cosine)
