import {
    findComicHistoryByuserIdDb,
    deleteHistoryByHistoryIdDb,
    addHistoryByUserIdDb,
    findChapterHistoryByuserIdandComicIdDb,
    findLatestChapterByUserIdandComicIdDb
} from "./history.repository.js"

const findComicHistory = async (userId) => {
    const result = await findComicHistoryByuserIdDb(userId)
    // if (!result) throw new Error("User doesnt have any History!")
    return result
}

const findChapterHistory = async (userId, searchData) => {
    const result = await findChapterHistoryByuserIdandComicIdDb(userId, searchData)
    // if (!result) throw new Error("User doesnt have any History in this Comic!")
    return result
}

const findLatestChapterHistory = async (userId, searchData) => {
    const result = await findLatestChapterByUserIdandComicIdDb(userId, searchData)
    // if (!result) throw new Error("User doesnt have any History in this Comic!")
    return result
}

const deleteHistory = async (comicId) => {
    const result = await deleteHistoryByHistoryIdDb(comicId)
    if (!result) throw new Error("failed deleting History")
    return result
}

const addHistory = async (userId, HistoryData) => {
    const HistoryDataResult = await addHistoryByUserIdDb(userId, HistoryData)
    if (!HistoryDataResult) throw new Error("failed adding History")
    return HistoryDataResult
}

export { findComicHistory, deleteHistory, addHistory, findChapterHistory, findLatestChapterHistory } 