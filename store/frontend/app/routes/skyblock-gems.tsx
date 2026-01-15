import type { Route } from "./+types/skyblock-gems";
import { CategoryPage } from "~/components/CategoryPage";
import { requireUser } from "~/lib/session.server";

export async function loader({ request }: Route.LoaderArgs) {
  await requireUser(request);
  return null;
}

export function meta(): Route.MetaDescriptors {
  return [{ title: "SkyBlock Gems" }];
}

export default function SkyBlockGems() {
  return (
    <CategoryPage
      title="SkyBlock Gems"
      description="Purchase gems to use in the SkyBlock store."
    />
  );
}

