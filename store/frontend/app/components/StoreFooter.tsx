import { Link } from "react-router";

const paymentMethods = [
  { name: "Mastercard", icon: "mastercard" },
  { name: "Visa", icon: "visa" },
  { name: "American Express", icon: "american-express" },
  { name: "PayPal", icon: "paypal" },
  { name: "iDEAL", icon: "ideal" },
  { name: "Bancontact", icon: "bancontact" },
  { name: "Apple Pay", icon: "apple-pay" },
  { name: "Google Pay", icon: "google-pay" },
] as const;

export function StoreFooter() {
  return (
    <footer className="bg-gray-950 border-t border-gray-800/50 mt-auto">
      <div className="mx-auto max-w-6xl px-4 py-8">
        <div className="flex flex-col lg:flex-row justify-between gap-8">
          {/* Legal Notice */}
          <div className="lg:max-w-xl">
            <h3 className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-3">
              Legal Notice
            </h3>
            <p className="text-xs text-gray-500 leading-relaxed">
              The Hypixel server is in no way affiliated with Mojang Studios, nor should it be considered a company endorsed by Mojang Studios. Any contributions or purchases made on this store goes to the Hypixel team.
            </p>
            <p className="text-xs text-gray-500 leading-relaxed mt-2">
              For support or a purchase history, please send us a ticket on the{" "}
              <Link to="#" className="text-gray-400 hover:text-gray-300 underline">
                Hypixel Support Help Desk
              </Link>
              . To manage or cancel your subscriptions, visit our{" "}
              <Link to="#" className="text-gray-400 hover:text-gray-300 underline">
                subscription portal
              </Link>
              .
            </p>
            <p className="text-xs text-gray-600 mt-2">
              Minecraft ®/TM, Mojang Studios / Notch © 2009-2026
            </p>
          </div>

          {/* Payment Methods */}
          <div className="lg:text-right">
            <h3 className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-3">
              Payment Methods We Accept
            </h3>
            <div className="flex flex-wrap gap-2 lg:justify-end">
              {paymentMethods.map((method) => (
                <div
                  key={method.icon}
                  className="bg-green-500 rounded px-2 py-1.5 flex items-center justify-center"
                  title={method.name}
                >
                  <img
                    src={`https://staticassets.hypixel.net/store/payments/${method.icon}.webp`}
                    alt={method.name}
                    className="h-5 w-auto"
                  />
                </div>
              ))}
            </div>
            <p className="text-xs text-gray-500 mt-3">
              All payments are handled and secured by Tebex.
            </p>
          </div>
        </div>
      </div>
    </footer>
  );
}

