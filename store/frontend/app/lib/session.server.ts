import { createCookieSessionStorage } from "react-router";

export type UserSession = {
  username: string;
};

const storage = createCookieSessionStorage({
  cookie: {
    name: "store_session",
    httpOnly: true,
    path: "/",
    sameSite: "lax",
    secrets: [process.env.SESSION_SECRET ?? "dev-secret-change-me"],
    secure: process.env.NODE_ENV === "production",
  },
});

export async function getUserSession(request: Request) {
  const cookie = request.headers.get("Cookie");
  return storage.getSession(cookie);
}

export async function getUser(request: Request): Promise<UserSession | null> {
  const session = await getUserSession(request);
  const username = session.get("username");
  if (typeof username !== "string" || username.trim().length === 0) return null;
  return { username };
}

export async function requireUser(
  request: Request,
  opts?: { returnTo?: string }
): Promise<UserSession> {
  const user = await getUser(request);
  if (user) return user;

  const url = new URL(request.url);
  const returnTo = opts?.returnTo ?? (url.pathname + url.search);
  const loginUrl = new URL("/login", url);
  loginUrl.searchParams.set("returnTo", returnTo);

  throw new Response(null, {
    status: 302,
    headers: {
      Location: loginUrl.toString(),
    },
  });
}

export function commitUserSession(session: Awaited<ReturnType<typeof getUserSession>>) {
  return storage.commitSession(session);
}

export function destroyUserSession(session: Awaited<ReturnType<typeof getUserSession>>) {
  return storage.destroySession(session);
}

