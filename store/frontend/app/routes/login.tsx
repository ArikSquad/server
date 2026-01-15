import type { Route } from "./+types/login";
import { Form, redirect, useSearchParams } from "react-router";
import { commitUserSession, getUserSession } from "~/lib/session.server";

function normalizeReturnTo(returnTo: string | null) {
  if (!returnTo) return "/";
  // Prevent open redirects
  if (!returnTo.startsWith("/")) return "/";
  if (returnTo.startsWith("//")) return "/";
  return returnTo;
}

export async function action({ request }: Route.ActionArgs) {
  const form = await request.formData();
  const username = String(form.get("username") ?? "").trim();
  const returnTo = normalizeReturnTo(String(form.get("returnTo") ?? "/"));

  if (!username) {
    return { error: "Please enter a Minecraft username." };
  }

  const session = await getUserSession(request);
  session.set("username", username);

  return redirect(returnTo, {
    headers: {
      "Set-Cookie": await commitUserSession(session),
    },
  });
}

export async function loader({ request }: Route.LoaderArgs) {
  const url = new URL(request.url);
  const returnTo = normalizeReturnTo(url.searchParams.get("returnTo"));
  return { returnTo };
}

export function meta(): Route.MetaDescriptors {
  return [{ title: "Login" }];
}

export default function Login({
  actionData,
  loaderData,
}: Route.ComponentProps) {
  const [searchParams] = useSearchParams();
  const returnTo = loaderData.returnTo ?? normalizeReturnTo(searchParams.get("returnTo"));

  return (
    <main className="mx-auto max-w-3xl px-4 py-12">
      <h1 className="text-4xl font-semibold mb-8">Login</h1>

      <div className="rounded-xl border border-gray-800 bg-gray-950/40 p-6">
        <p className="text-gray-300 mb-6 text-sm">
          Enter your Minecraft username to continue.
        </p>

        <Form method="post" className="space-y-4">
          <input type="hidden" name="returnTo" value={returnTo} />

          <label className="block">
            <span className="block text-sm text-gray-200 mb-2">
              Enter your Minecraft username
            </span>
            <input
              name="username"
              autoComplete="username"
              className="w-full rounded-full bg-white text-gray-900 px-6 py-3 outline-none"
              placeholder="Enter your in-game username"
            />
          </label>

          {actionData && "error" in actionData && actionData.error ? (
            <p className="text-red-300 text-sm">{actionData.error}</p>
          ) : null}

          <button
            type="submit"
            className="w-full rounded-full bg-amber-300 text-gray-900 py-3 font-medium hover:bg-amber-200"
          >
            Continue
          </button>
        </Form>
      </div>
    </main>
  );
}

