import type { Route } from "./+types/_index";
import { CategoryGrid } from "~/components/CategoryGrid";
import { Link } from "react-router";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Hypixel Store" },
    {
      name: "description",
      content: "Browse store categories and purchase items.",
    },
  ];
}

export default function Index() {
  return (
    <main className="mx-auto max-w-6xl px-4 py-10">
      <h1 className="text-3xl font-semibold mb-6 text-white">Hypixel Store</h1>
      <CategoryGrid />

      <section className="mt-12 space-y-8 text-gray-200">
        <div>
          <h2 className="text-xl font-semibold mb-2 text-white">Welcome</h2>
          <p className="text-gray-400 text-sm leading-relaxed">
            Welcome to the official Hypixel Store! This is the place for you to
            enhance your Hypixel Server experience. We offer ranks, Hypixel Gold,
            SkyBlock Gems, and more. You can choose the product category in the
            site navigation at the top or by clicking on the category list above.
          </p>
          <p className="text-gray-400 text-sm leading-relaxed mt-2">
            All payments are handled and secured by Tebex.
          </p>
        </div>

        <div>
          <h2 className="text-xl font-semibold mb-2 text-white">About Hypixel</h2>
          <p className="text-gray-400 text-sm leading-relaxed">
            Starting as a YouTube channel making Minecraft Adventure Maps, Hypixel
            is now one of the world's largest and highest-quality Minecraft Server
            Networks, featuring hit games such as SkyBlock, The Walls, Bed Wars,
            Blitz Survival Games, and many more.
          </p>
        </div>

        <div>
          <h2 className="text-xl font-semibold mb-2 text-white">Need help?</h2>
          <p className="text-gray-400 text-sm leading-relaxed">
            If you have any questions or issues related to payments,{" "}
            <Link
              to="#"
              className="text-amber-400 hover:text-amber-300 underline"
            >
              send us a ticket here
            </Link>
            , and we will reply as fast as possible.
          </p>
        </div>
      </section>
    </main>
  );
}
