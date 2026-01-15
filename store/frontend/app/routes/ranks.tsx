import type { Route } from "./+types/ranks";
import { CategoryPage } from "~/components/CategoryPage";
import { requireUser } from "~/lib/session.server";

export async function loader({ request }: Route.LoaderArgs) {
  await requireUser(request);
  return null;
}

export function meta(): Route.MetaDescriptors {
  return [{ title: "Ranks" }];
}

export default function Ranks() {
  return (
    <CategoryPage
      title="Ranks"
      description="Upgrade your rank and unlock perks across the network."
    />
  );
}

