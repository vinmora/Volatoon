import express from "express";
import cors from "cors";
import dotenv from "dotenv";
import authController from "./auth/auth.controller.js";
import userController from "./user/user.controller.js";
import bookmarkController from "./bookmark/bookmark.controller.js";
import historyController from "./history/history.controller.js";
import commentController from "./comment/comment.controller.js";
import voucherController from "./voucher/voucher.controller.js";

// import unknownEndPoint from './middleware/unknownEndpoint.js';
const app = express();

dotenv.config();
const port = 2000;
app.use(express.json());
app.use(cors());

app.get("/", (req, res) => {
  res.send("Volatoon api! is here");
});

app.use("/api/auth/", authController);
app.use("/api/", userController);
app.use("/api/", bookmarkController);
app.use("/api/", historyController);
app.use("/api/", commentController);
app.use("/api/", voucherController);


app.listen(port, () => {
  console.log(`Volatoon is listening on port ${port}`);
});
