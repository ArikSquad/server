import type { Route } from "./+types/gold";
import { CategoryPage } from "~/components/CategoryPage";
import { requireUser } from "~/lib/session.server";

export async function loader({ request }: Route.LoaderArgs) {
  await requireUser(request);
  return null;
}

export function meta(): Route.MetaDescriptors {
  return [{ title: "Gold" }];
}

export default function Gold() {
  return (
    <CategoryPage
      title="Gold"
      description="Buy gold packs to use across supported game modes."
    />
  );
}

