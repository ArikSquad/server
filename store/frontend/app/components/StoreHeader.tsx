import { Link } from "react-router";
import { craftHeadAvatarUrl } from "~/lib/crafthead";
import { ChevronDown, HelpCircle } from "lucide-react";

const navLinks = [
  { to: "/ranks", label: "Ranks" },
  { to: "/gold", label: "Gold" },
  { to: "/skyblock-gems", label: "SkyBlock Gems" },
  { to: "/housing-plus", label: "Housing+" },
  { to: "/boosters", label: "Boosters" },
  { to: "/companions", label: "Companions" },
] as const;

export function StoreHeader({
  user,
}: {
  user: null | {
    username: string;
  };
}) {
  return (
    <header className="relative">
      {/* Classic border texture background */}
      <div
        className="absolute inset-0 bg-cover bg-center bg-no-repeat"
        style={{
          backgroundImage: `url('https://staticassets.hypixel.net/store/borders/classic-border.webp')`,
          backgroundSize: "cover",
        }}
      />
      {/* Dark overlay for readability */}
      <div className="absolute inset-0 bg-gradient-to-b from-black/40 to-black/60" />

      <div className="relative z-10 mx-auto max-w-6xl px-4 py-3">
        <div className="flex items-center justify-between gap-4">
          {/* Hypixel Logo */}
          <Link to="/" className="flex-shrink-0">
            <img
              src="https://dunb17ur4ymx4.cloudfront.net/webstore/logos/6c9b0cbd5c2f0ceef98f01068102b0d056c04b7b.png"
              alt="Hypixel"
              className="h-10 w-auto"
            />
          </Link>

          {/* Navigation */}
          <nav className="hidden lg:flex items-center">
            <div className="flex items-center bg-gray-900/80 rounded-lg overflow-hidden border border-gray-700/50">
              {navLinks.map((link) => (
                <Link
                  key={link.to}
                  to={link.to}
                  className="px-4 py-2 text-sm text-gray-200 hover:text-white hover:bg-gray-800/80 transition-colors border-r border-gray-700/50 last:border-r-0"
                >
                  {link.label}
                </Link>
              ))}
            </div>
          </nav>

          {/* Right section: User info, currency, help */}
          <div className="flex items-center gap-2">
            {user ? (
              <div className="flex items-center gap-2 bg-gray-900/80 rounded-lg px-3 py-1.5 border border-gray-700/50">
                <img
                  src={craftHeadAvatarUrl(user.username, 32)}
                  alt=""
                  className="h-7 w-7 rounded bg-gray-800"
                  width={32}
                  height={32}
                  loading="eager"
                />
                <span className="text-sm text-gray-100 font-medium">
                  {user.username}
                </span>
              </div>
            ) : (
              <Link
                className="flex items-center gap-2 bg-gray-900/80 rounded-lg px-3 py-1.5 border border-gray-700/50 text-sm text-gray-100 hover:text-white transition-colors"
                to="/login"
              >
                Login
              </Link>
            )}

            {/* Currency selector */}
            <button className="flex items-center gap-1 bg-gray-900/80 rounded-lg px-3 py-1.5 border border-gray-700/50 text-sm text-gray-100 hover:text-white transition-colors">
              USD
              <ChevronDown className="h-4 w-4" />
            </button>

            {/* Help button */}
            <button className="flex items-center justify-center bg-gray-900/80 rounded-lg p-2 border border-gray-700/50 text-gray-100 hover:text-white transition-colors">
              <HelpCircle className="h-5 w-5" />
            </button>
          </div>
        </div>
      </div>
    </header>
  );
}
