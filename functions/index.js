const functions = require("firebase-functions");
const admin = require("firebase-admin");
const nodemailer = require("nodemailer");

admin.initializeApp();

exports.sendSaleEmails = functions.https.onRequest(async (req, res) => {
  const usersSnapshot = await admin.auth().listUsers();
  const userEmails = usersSnapshot.users.map((user) => user.email);

  const transporter = nodemailer.createTransport({
    service: "gmail",
    auth: {
      user: "your-email@gmail.com",
      pass: "your-email-password",
    },
  });

  const mailOptions = {
    from: "your-email@gmail.com",
    to: userEmails.join(","),
    subject: "Sale Announcement",
    text: "There is a sale happening in our app. Check it out!",
  };

  try {
    await transporter.sendMail(mailOptions);
    res.status(200).send("Sale emails sent successfully.");
  } catch (error) {
    console.error("Error sending sale emails:", error);
    res.status(500).send("Error sending sale emails.");
  }
});
