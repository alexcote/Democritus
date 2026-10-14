const CACHE_NAME = 'democritus-v1';

const ASSETS_TO_CACHE = [
  './',
  './index.html',
  './manifest.json',
  './responsive.css',
  './Textures/iepback.gif',
  './Icons/favicon-32x32.png',
  './Icons/demicon-192.png',
  './Icons/demicon-512.png',
  './Icons/contents.gif',
  './Pictures/mydemocritus.jpg',
  './Pictures/atom-animation.gif',
  './Pictures/gr_band.gif',
  './Font/Wide-Latin.ttf'
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      // Use individual promises or addAll to cache shell
      return cache.addAll(ASSETS_TO_CACHE);
    })
  );
  self.skipWaiting();
});

// Activate: Purge old cache versions
self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) =>
      Promise.all(
        keys.map((key) => {
          if (key !== CACHE_NAME) return caches.delete(key);
        })
      )
    )
  );
  self.clients.claim();
});

// Fetch: Stale-While-Revalidate or Cache-First with Runtime Caching
self.addEventListener('fetch', (event) => {
  // Only handle HTTP/HTTPS GET requests
  if (event.request.method !== 'GET' || !event.request.url.startsWith('http')) {
    return;
  }

  event.respondWith(
    caches.match(event.request).then((cachedResponse) => {
      if (cachedResponse) {
        return cachedResponse;
      }

      // If not in cache, fetch from network and store it dynamically
      return fetch(event.request).then((networkResponse) => {
      // Allow both local ('basic') and cross-origin fonts/assets ('cors') to be cached
      if (!networkResponse || networkResponse.status !== 200 || (networkResponse.type !== 'basic' && networkResponse.type !== 'cors')) {
         return networkResponse;
      }

        // Clone response because it can only be consumed once
        const responseToCache = networkResponse.clone();
        caches.open(CACHE_NAME).then((cache) => {
          cache.put(event.request, responseToCache);
        });

        return networkResponse;
      }).catch(() => {
        // Optional offline fallback if a page wasn't cached yet
        if (event.request.headers.get('accept')?.includes('text/html')) {
          return caches.match('./index.html');
        }
      });
    })
  );
});