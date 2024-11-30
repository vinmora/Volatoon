import { findCommentsByChapterIdDb, addCommentDb, deleteCommentDb, likeCommentDb } from "./comment.repository.js"

const getComments = async (chapterId) => {
    const comments = await findCommentsByChapterIdDb(chapterId)
    return comments
}

const addComment = async (userId, chapterId, content) => {
    if (!content.trim()) throw new Error("Comment cannot be empty")
    const comment = await addCommentDb(userId, chapterId, content)
    return comment
}

const deleteComment = async (commentId) => {
    const comment = await deleteCommentDb(commentId)
    return comment
}

const likeComment = async (commentId) => {
    const comment = await likeCommentDb(commentId)
    return comment
}

export {
    getComments,
    addComment,
    deleteComment,
    likeComment
}
