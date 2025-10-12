import axios from "axios";

export const checkForAlert = async () => {
  try {
    const res = await axios.get("http://localhost:5174/alert/check");
    return { success: true, data: res.data.hasAlert };
  } catch (err) {
    return { success: false, data: false };
  }
};

export const clearAlert = async () => {
  try {
    await axios.post("http://localhost:5174/alert/clear");
  } catch (err) {
    console.error("Error clearing alert:", err);
  }
};

