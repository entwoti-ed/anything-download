const isProduction = process.env.NODE_ENV === 'production';
export let BASE_URL = isProduction ? "" : "http://127.0.0.1:10086";
