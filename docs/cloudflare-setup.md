# Cloudflare Worker Setup

Free exit alternative to a VPS. One Worker handles **HTTP relay** (desktop) and **TCP tunnel** (Android). Deploy with Wrangler only — it uploads the code and registers the `TUNNEL_HUB` Durable Object binding Android needs.

## Deploy

```bash
cd relay/deploy/cloudflare
npm install -g wrangler   # once
wrangler login            # once
```

Edit before deploy:

1. **`worker.js`** — `WORKER_HOST = "your-worker.your-subdomain.workers.dev"` (hostname only, no `https://`)
2. **`wrangler.toml`** — `name = "your-worker-name"` (same name as in the Cloudflare dashboard)

```bash
wrangler deploy
```

Confirm output includes `env.TUNNEL_HUB (TunnelHub)` and your `*.workers.dev` URL.

To update later: same `wrangler deploy` from this folder.

## Apps Script

In [`relay/deploy/apps-script/Code.gs`](../relay/deploy/apps-script/Code.gs):

```js
const EXIT_RELAY_URL = "https://your-worker.your-subdomain.workers.dev";
const EXIT_TUNNEL_URL = "";   // empty → same host + /tunnel
const EXIT_RELAY_KEY = "";    // empty unless RELAY_KEY is set in wrangler.toml
```

Redeploy Apps Script: **Deploy → Manage deployments → New version**.

Optional split exit (Worker relay + VPS tunnel):

```js
const EXIT_TUNNEL_URL = "http://YOUR_VPS_IP:8787/tunnel";
```

## Verify

```bash
curl -s -X POST "https://your-worker.workers.dev/tunnel" \
  -H "Content-Type: application/json" \
  -d '{"op":"open","id":"test-1","target":"149.154.167.92:443"}'
```

Expect `{"ok":true}`. If you see `missing TUNNEL_HUB binding`, run `wrangler deploy` again from `relay/deploy/cloudflare/`.

## Worker vs VPS

| | Worker | VPS |
|---|---|---|
| Cost | Free tier | ~$5/mo |
| Android tunnel / Telegram | Yes | Yes |
| Desktop HTTP relay | Yes | Yes |
| Sites on Cloudflare IPs (e.g. ChatGPT) | No — Workers block outbound TCP to CF ranges | Yes |
| CAPTCHAs / fixed IP | No / No | Yes / Yes |

Free tier: ~100k requests/day; tunnel polls use Durable Object time. Heavy use → second Worker account or VPS.

Files: [`worker.js`](../relay/deploy/cloudflare/worker.js), [`wrangler.toml`](../relay/deploy/cloudflare/wrangler.toml).
