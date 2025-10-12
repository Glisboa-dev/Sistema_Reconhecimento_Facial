import apiClient from "./apiClient";

export const searchPresences = async (filters = {}, pageable = {}) => {
  const params = { ...filters, ...pageable };

  if (params.from) {
    const fromDate = params.from instanceof Date ? params.from : new Date(params.from);
    params.from = fromDate.toISOString().slice(0, 19);
  }
  
  if (params.to) {
    const toDate = params.to instanceof Date ? params.to : new Date(params.to);
    toDate.setHours(23, 59, 59, 999);
    params.to = toDate.toISOString().slice(0, 19);
  }

  try {
    const res = await apiClient.get("/presences/search", { params });
    return res; 
  } catch (err) {
    throw err;
  }
};
