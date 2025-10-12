import apiClient from "./apiClient";

export const searchEmployees = async (filters = {}, pageable = {}) => {
  const params = { ...filters, ...pageable };

  try {
    const res = await apiClient.get("/employees/search", { params });
    return res; 
  } catch (err) {
    throw err;
  }
};
