import type { Route } from "./+types/_user_boosters";
import { CategoryPage } from "~/components/CategoryPage";
import { requireUser } from "~/lib/session.server";

export async function loader({ request }: Route.LoaderArgs) {
  await requireUser(request);
  return null;
}

export function meta(): Route.MetaDescriptors {
  return [{ title: "Boosters" }];
}

export default function Boosters() {
  return (
    <CategoryPage
      title="Boosters"
      description="Get global boosters to help everyone earn more rewards."
    />
  );
}

