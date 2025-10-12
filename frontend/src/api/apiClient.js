import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://127.0.0.1:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

export const setAuthToken = (token) => {
  if (token) {
    localStorage.setItem("authToken", token);
  } else {
    localStorage.removeItem("authToken");
  }
};

apiClient.interceptors.request.use(
  (config) => {
    if (!config.url.includes("/login")) {
      const token = localStorage.getItem("authToken");
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
  (response) => {
    if (response.data.success) {
      return {
        success: true,
        message: response.data.message,
        data: response.data.data,
      };
    }

    return Promise.reject({
      success: false,
      message: response.data.message || "Unexpected response",
    });
  },
  (error) => {
    const res = error.response;
    const formattedError = {
      success: false,
      error: res?.data?.error || "Unknown Error",
      message: res?.data?.message || error.message,
      status: res?.data?.status || res?.status || 500,
      timestamp: res?.data?.timestamp || new Date().toISOString(),
    };

    if (formattedError.status === 401) {
      localStorage.removeItem("authToken");
      window.location.href = "/login";
    }

    return Promise.reject(formattedError);
  }
);

export default apiClient;
