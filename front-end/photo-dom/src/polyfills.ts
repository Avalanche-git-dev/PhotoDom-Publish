(window as any).global = window;
(window as any).Buffer = (window as any).Buffer || require('buffer').Buffer;
(window as any).process = require('process'); // Polyfill per process
