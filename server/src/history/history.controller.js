import { Router } from 'express';
import {
    findComicHistory,
    deleteHistory,
    addHistory,
    findChapterHistory,
    findLatestChapterHistory
} from './history.service.js';
import authenticateToken from '../middleware/token.auth.js';

const router = Router();

router.get("/history", authenticateToken, async (req, res) => {
    const { userId } = req.user;

    try {
        const data = await findComicHistory(userId)

        res.status(200).json({
            status: 200,
            message: "User History retrieved successfully",
            data
        })
    } catch (err) {
        return res.status(401).json({
            status: 401,
            message: err.message
        })
    }
})

router.delete("/history/:comicId", authenticateToken, async (req, res) => {

    const comicId = req.params.comicId;

    console.log(comicId)

    try {
        const historyDataDelete = await deleteHistory(comicId)

        res.status(200).json({
            status: 200,
            message: "User history Deleted",
            data: {
                historyDataDelete
            }
        })
    } catch (err) {
        return res.status(404).json({
            status: 404,
            message: err.message
        })
    }

})

router.post("/history", authenticateToken, async (req, res) => {
    const { userId } = req.user;
    const historyData = req.body

    try {
        const Result = await addHistory(userId, historyData)

        res.status(201).json({
            status: 201,
            message: "User History added successfully",
            data: {
                Result
            }
        })
    } catch (err) {
        return res.status(401).json({
            status: 401,
            message: err.message
        })
    }

})


// api untuk nampilin semua history user di satu buah komik
router.get("/history/:comicId", authenticateToken, async (req, res) => {
    const { userId } = req.user;
    const comicId = req.params.comicId;

    console.log(comicId)

    try {
        const Result = await findChapterHistory(userId, comicId)

        res.status(201).json({
            status: 201,
            message: "User History retrieved successfully",
            data: {
                Result
            }
        })
    } catch (err) {
        return res.status(401).json({
            status: 401,
            message: err.message
        })
    }

})

router.get("/history/latest-chapter", authenticateToken, async (req, res) => {
    const { userId } = req.user;
    const comicId = req.params.comicId;

    console.log(comicId)

    try {
        const Result = await findLatestChapterHistory(userId, historyData)

        res.status(201).json({
            status: 201,
            message: "User History retrieved successfully",
            data: {
                Result
            }
        })
    } catch (err) {
        return res.status(401).json({
            status: 401,
            message: err.message
        })
    }

})

export default router 