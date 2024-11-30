import { Router } from 'express';
import { getComments, addComment, deleteComment, likeComment } from './comment.service.js';
import authenticateToken from '../middleware/token.auth.js';

const router = Router();

router.get("/comments/:chapterId", authenticateToken, async (req, res) => {
    try {
        const comments = await getComments(req.params.chapterId)
        res.status(200).json({
            status: 200,
            message: "Comments retrieved successfully",
            data: comments
        })
    } catch (err) {
        res.status(400).json({
            status: 400,
            message: err.message
        })
    }
})

router.post("/comments", authenticateToken, async (req, res) => {
    try {
        const { userId } = req.user;
        const { chapter_id, content } = req.body;
        const comment = await addComment(userId, chapter_id, content)
        res.status(201).json({
            status: 201,
            message: "Comment added successfully",
            data: [comment]
        })
    } catch (err) {
        res.status(400).json({
            status: 400,
            message: err.message
        })
    }
})

router.delete("/comments/:commentId", authenticateToken, async (req, res) => {
    try {
        const comment = await deleteComment(req.params.commentId)
        res.status(200).json({
            status: 200,
            message: "Comment deleted successfully",
            data: [comment]
        })
    } catch (err) {
        res.status(400).json({
            status: 400,
            message: err.message
        })
    }
})

router.post("/comments/:commentId/like", authenticateToken, async (req, res) => {
    try {
        const comment = await likeComment(req.params.commentId)
        res.status(200).json({
            status: 200,
            message: "Comment liked successfully",
            data: [comment]
        })
    } catch (err) {
        res.status(400).json({
            status: 400,
            message: err.message
        })
    }
})

export default router;
