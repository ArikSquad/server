export function craftHeadAvatarUrl(username: string, size = 32) {
  const safe = encodeURIComponent(username);
  return `https://crafthead.net/avatar/${safe}/${size}`;
}

