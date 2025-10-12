import apiClient, { setAuthToken } from "./apiClient";

export const loginRequest = async (username, password) => {
  const res = await apiClient.post("/auth/login", { username, password });

  const token = res.data?.token || res.data?.data?.token;
  const role = res.data?.role || res.data?.data?.role;

  if (token) setAuthToken(token);

  if (role) localStorage.setItem("userRole", role);

  return { token, role };
};

export const registerRequest = async (userData) => {
  return apiClient.post("/auth/register", userData);
};

export const registerEmployee = async (username, password, employeeRecordId) => {
  const requestBody = { username, password, employeeRecordId };

  try {
    const res = await apiClient.post("/auth/register/employee", requestBody);
    return res; 
  } catch (error) {
    throw error;
  }
};
