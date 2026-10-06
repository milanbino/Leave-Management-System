let globalData = {
  users: [
    { id: 101, name: "Alex M", email: "alex11@gmail.com", dept: "IT", role: "Employee", password: "alex11" },
    { id: 202, name: "NIDHILA", email: "nidhila@gmail.com", dept: "Marketing", role: "Manager", password: "nidhila123" },
    { id: 102, name: "Anu", email: "Anu12@gmail.com", dept: "Finance", role: "Employee", password: "anu123" },
    { id: 103, name: "Arya", email: "Arya123@gmail.com", dept: "Marketing", role: "Employee", password: "arya@123" },
    { id: 104, name: "Tojo", email: "tojo@gmail.com", dept: "Sales", role: "Employee", password: "tojo12" }
  ],
  leaveRequests: [],
  nextRequestId: 1001
};

export default function handler(req, res) {
  // CORS Headers
  res.setHeader('Access-Control-Allow-Credentials', true);
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET,OPTIONS,PATCH,DELETE,POST,PUT');
  res.setHeader('Access-Control-Allow-Headers', 'X-CSRF-Token, X-Requested-With, Accept, Accept-Version, Content-Length, Content-MD5, Content-Type, Date, X-Api-Version');

  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  if (req.method === 'GET') {
    return res.status(200).json({ data: globalData });
  }

  if (req.method === 'POST' || req.method === 'PUT') {
    try {
      let bodyData = req.body;
      if (typeof bodyData === 'string') {
        bodyData = JSON.parse(bodyData);
      }
      if (bodyData && bodyData.data) {
        bodyData = bodyData.data;
      }

      if (bodyData) {
        if (Array.isArray(bodyData.users)) {
          bodyData.users.forEach(nu => {
            if (nu.name !== "Milan Bino" && nu.id !== 201 && !globalData.users.some(u => u.id === nu.id)) {
              globalData.users.push(nu);
            }
          });
        }

        if (Array.isArray(bodyData.leaveRequests)) {
          bodyData.leaveRequests.forEach(nr => {
            const existing = globalData.leaveRequests.find(r => r.id === nr.id);
            if (!existing) {
              globalData.leaveRequests.push(nr);
            } else {
              existing.status = nr.status;
            }
          });
        }

        if (bodyData.nextRequestId && bodyData.nextRequestId > globalData.nextRequestId) {
          globalData.nextRequestId = bodyData.nextRequestId;
        }
      }
    } catch (e) {
      console.error("Sync handler error", e);
    }

    return res.status(200).json({ success: true, data: globalData });
  }

  return res.status(405).json({ error: 'Method not allowed' });
}
