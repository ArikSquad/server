import { Link } from "react-router";

const categories = [
  { to: "/ranks", label: "Ranks", url: "https://staticassets.hypixel.net/store/icons/ranks/mvp-plus.png" },
  { to: "/gold", label: "Gold", url: "https://staticassets.hypixel.net/store/icons/gold/4.png" },
  { to: "/skyblock-gems", label: "SkyBlock Gems", url: "https://staticassets.hypixel.net/store/icons/gems/5.png" },
  { to: "/housing-plus", label: "Housing+", url: "https://staticassets.hypixel.net/store/icons/housing-plus/icon.png" },
  { to: "/boosters", label: "Boosters", url: "https://staticassets.hypixel.net/store/icons/boosters/uhc.png" },
  { to: "/companions", label: "Companions", url: "https://staticassets.hypixel.net/store/icons/companions/icon.png" },
] as const;

export function CategoryGrid() {
  return (
    <section className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
      {categories.map((c) => (
        <Link
          key={c.to}
          to={c.to}
          className="group relative rounded-lg border border-gray-700/60 bg-[#212121] hover:border-gray-600/70 transition-all duration-200 overflow-hidden"
        >
          <div className="flex flex-col items-center justify-center py-10 px-6">
            <div className="w-24 h-24 flex items-center justify-center mb-4">
              <img
                src={c.url}
                alt={c.label}
                className="max-w-full max-h-full object-contain drop-shadow-lg group-hover:scale-105 transition-transform duration-200"
              />
            </div>
            <span className="text-amber-300/90 group-hover:text-amber-200 font-medium text-lg transition-colors">
              {c.label}
            </span>
          </div>
        </Link>
      ))}
    </section>
  );
}
