import apiClient from "./apiClient";

export const addStudentRecord = async (name, studentRequest, file) => {
  const form = new FormData();
  form.append("name", name);
  form.append(
    "student",
    new Blob([JSON.stringify(studentRequest)], { type: "application/json" })
  );
  form.append("file", file);

  try {
    const res = await apiClient.post("/records/add-student", form, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return res;
  } catch (err) {
    throw err;
  }
};

export const addEmployeeRecord = async (name, employeeRequest, file) => {
  const form = new FormData();
  form.append("name", name);
  form.append(
    "employee",
    new Blob([JSON.stringify(employeeRequest)], { type: "application/json" })
  );
  form.append("file", file);

  try {
    const res = await apiClient.post("/records/add-employee", form, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return res;
  } catch (err) {
    throw err;
  }
};

export const updateRecordName = async (id, newName) => {
  try {
    const res = await apiClient.put(`/records/update-name/${id}`, null, {
      params: { newName },
    });
    return res;
  } catch (err) {
    throw err;
  }
};

export const deleteRecordById = async (id) => {
  try {
    const res = await apiClient.delete(`/records/delete/${id}`);
    return res;
  } catch (err) {
    throw err;
  }
};

export const updateRecordPhoto = async (id, file) => {
  const form = new FormData();
  form.append("file", file);

  try {
    const res = await apiClient.put(`/records/update-photo/${id}`, form, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return res;
  } catch (err) {
    throw err;
  }
};

export const deactivateRecord = async (id) => {
  try {
    const res = await apiClient.put(`/records/deactivate/${id}`);
    return res;
  } catch (err) {
    throw err;
  }
};

export const activateRecord = async (id) => {
  try {
    const res = await apiClient.put(`/records/activate/${id}`);
    return res;
  } catch (err) {
    throw err;
  }
};
