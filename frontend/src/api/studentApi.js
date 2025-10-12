import apiClient from "./apiClient";

export const searchStudents = async (filters = {}, pageable = {}) => {
  const params = { ...filters, ...pageable };

  try {
    const res = await apiClient.get("/students/search", { params });
    return res;
  } catch (err) {
    throw err;
  }
};
