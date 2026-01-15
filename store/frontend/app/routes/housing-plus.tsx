import type { Route } from "./+types/housing-plus";
import { CategoryPage } from "~/components/CategoryPage";
import { requireUser } from "~/lib/session.server";

export async function loader({ request }: Route.LoaderArgs) {
  await requireUser(request);
  return null;
}

export function meta(): Route.MetaDescriptors {
  return [{ title: "Housing+" }];
}

export default function HousingPlus() {
  return (
    <CategoryPage
      title="Housing+"
      description="Enhance your housing experience with premium features."
    />
  );
}

