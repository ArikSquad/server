import { Outlet } from "react-router";
import * as StoreHeaderModule from "~/components/StoreHeader";
import { StoreFooter } from "~/components/StoreFooter";
import { getUser } from "~/lib/session.server";
import type { Route } from "./+types/+layout";

export async function loader({ request }: Route.LoaderArgs) {
    const user = await getUser(request);
    return { user };
}

export default function StoreLayout(loaderData: Route.LoaderArgs) {
    return (
        <div className="min-h-screen flex flex-col bg-[#1a1f2e] text-white">
            <div className="fixed inset-0 bg-linear-to-b from-[#2a1810] via-[#1a1f2e] to-[#0f1419] pointer-events-none" />

            <div className="relative z-10 flex flex-col min-h-screen">
                <StoreHeaderModule.StoreHeader user={loaderData.user} />
                <div className="flex-1">
                    <Outlet />
                </div>
                <StoreFooter />
            </div>
        </div>
    );
}