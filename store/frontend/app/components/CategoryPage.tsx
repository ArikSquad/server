import { Link } from "react-router";

export function CategoryPage({
  title,
  description,
}: {
  title: string;
  description: string;
}) {
  return (
    <main className="mx-auto max-w-6xl px-4 py-10">
      <div className="mb-6">
        <h1 className="text-3xl font-semibold text-white">{title}</h1>
        <p className="text-gray-400 mt-2">{description}</p>
      </div>

      <div className="rounded-lg border border-gray-700/60 bg-gray-900/50 p-6">
        <p className="text-gray-400 text-sm">
          This is a placeholder SSR page. Hook it up to your catalog/checkout
          service when you're ready.
        </p>
        <div className="mt-4">
          <Link className="text-amber-400 hover:text-amber-300" to="/">
            ← Back to Store
          </Link>
        </div>
      </div>
    </main>
  );
}
