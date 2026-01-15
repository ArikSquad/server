import type { Route } from "./+types/companions";
import { CategoryPage } from "~/components/CategoryPage";
import { requireUser } from "~/lib/session.server";

export async function loader({ request }: Route.LoaderArgs) {
  await requireUser(request);
  return null;
}

export function meta(): Route.MetaDescriptors {
  return [{ title: "Companions" }];
}

export default function Companions() {
  return (
    <CategoryPage
      title="Companions"
      description="Pick up companions to follow you around in-game."
    />
  );
}

