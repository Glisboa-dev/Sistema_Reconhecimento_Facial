export const formatCPF = (cpf) => {
  if (!cpf) return '';
  const numbers = cpf.replace(/\D/g, '');
  if (numbers.length !== 11) return cpf;
  return numbers.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
};

export const removeCPFFormatting = (cpf) => {
  if (!cpf) return '';
  return cpf.replace(/\D/g, '');
};

export const validateCPF = (cpf) => {
  const numbers = removeCPFFormatting(cpf);
  
  if (numbers.length !== 11) {
    return { valid: false, message: 'CPF deve conter 11 dígitos' };
  }
  
  if (/^(\d)\1+$/.test(numbers)) {
    return { valid: false, message: 'CPF inválido' };
  }
  
  let sum = 0;
  let remainder;
  
  for (let i = 1; i <= 9; i++) {
    sum += parseInt(numbers.substring(i - 1, i)) * (11 - i);
  }
  
  remainder = (sum * 10) % 11;
  if (remainder === 10 || remainder === 11) remainder = 0;
  if (remainder !== parseInt(numbers.substring(9, 10))) {
    return { valid: false, message: 'CPF inválido' };
  }
  
  sum = 0;
  for (let i = 1; i <= 10; i++) {
    sum += parseInt(numbers.substring(i - 1, i)) * (12 - i);
  }
  
  remainder = (sum * 10) % 11;
  if (remainder === 10 || remainder === 11) remainder = 0;
  if (remainder !== parseInt(numbers.substring(10, 11))) {
    return { valid: false, message: 'CPF inválido' };
  }
  
  return { valid: true, message: '' };
};

export const handleCPFInput = (value) => {
  const numbers = value.replace(/\D/g, '');
  if (numbers.length <= 11) {
    if (numbers.length <= 3) return numbers;
    if (numbers.length <= 6) return numbers.replace(/(\d{3})(\d+)/, '$1.$2');
    if (numbers.length <= 9) return numbers.replace(/(\d{3})(\d{3})(\d+)/, '$1.$2.$3');
    return numbers.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }
  return value.slice(0, 14);
};

