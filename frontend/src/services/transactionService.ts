import apiClient from './api';

export const transactionService = {
  createTransaction: async (data: any) => {
    const response = await apiClient.post('/transactions', data);
    return response.data;
  },

  getTransaction: async (id: string) => {
    const response = await apiClient.get(`/transactions/${id}`);
    return response.data;
  },

  getTransactions: async (page = 0, size = 20) => {
    const response = await apiClient.get('/transactions', {
      params: { page, size },
    });
    return response.data;
  },

  getSentTransactions: async (page = 0, size = 20) => {
    const response = await apiClient.get('/transactions/sent', {
      params: { page, size },
    });
    return response.data;
  },

  getReceivedTransactions: async (page = 0, size = 20) => {
    const response = await apiClient.get('/transactions/received', {
      params: { page, size },
    });
    return response.data;
  },
};
