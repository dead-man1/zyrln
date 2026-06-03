# راه‌اندازی Cloudflare Worker

خروجی رایگان به‌جای VPS. یک Worker هم **رله HTTP** (دسکتاپ) هم **تونل TCP** (اندروید) را دارد. فقط با Wrangler دیپلوی کن — binding مورد نیاز اندروید (`TUNNEL_HUB`) خودکار ثبت می‌شود.

## دیپلوی

<div dir="ltr" align="left" style="direction: ltr; text-align: left;">

```bash
cd relay/deploy/cloudflare
npm install -g wrangler   # یک بار
wrangler login            # یک بار
```

</div>

قبل از دیپلوی:

1. **`worker.js`** — `WORKER_HOST = "your-worker.subdomain.workers.dev"` (بدون `https://`)
2. **`wrangler.toml`** — `name = "نام-worker-در-داشبورد"`

<div dir="ltr" align="left" style="direction: ltr; text-align: left;">

```bash
wrangler deploy
```

</div>

باید `env.TUNNEL_HUB (TunnelHub)` و آدرس `*.workers.dev` را ببینی.

## Apps Script

<div dir="ltr" align="left" style="direction: ltr; text-align: left;">

```js
const EXIT_RELAY_URL = "https://your-worker.workers.dev";
const EXIT_TUNNEL_URL = "";
const EXIT_RELAY_KEY = "";
```

</div>

Apps Script را دوباره دیپلوی کن.

## تست

<div dir="ltr" align="left" style="direction: ltr; text-align: left;">

```bash
curl -s -X POST "https://your-worker.workers.dev/tunnel" \
  -H "Content-Type: application/json" \
  -d '{"op":"open","id":"test-1","target":"149.154.167.92:443"}'
```

</div>

پاسخ: `{"ok":true}`. خطای `missing TUNNEL_HUB binding` → دوباره `wrangler deploy`.

## Worker در برابر VPS

| | Worker | VPS |
|---|---|---|
| تونل اندروید / تلگرام | بله | بله |
| سایت‌های روی IP Cloudflare | خیر | بله |
| CAPTCHA / IP ثابت | خیر | بله |

فایل‌ها: [`worker.js`](../../relay/deploy/cloudflare/worker.js)، [`wrangler.toml`](../../relay/deploy/cloudflare/wrangler.toml).
