import prisma from "../config/db.config.js"
import axios from "axios"

const findComicHistoryByuserIdDb = async (userId) => {
    const result = await prisma.history.findMany({
        where: { userId },
        orderBy: {
            createdAt: 'desc', // Ensures the latest record comes first
        },
        distinct: ['komik_id']
    });

    // Fetch comic details from the external API for each comic_id
    const historyWithComicDetails = await Promise.all(
        result.map(async (history) => {
            try {
                const response = await axios.get(`https://api-otaku.vercel.app/api/komik/${history.komik_id}`);
                const { title, type, genres, status, score, image } = response.data;
                return {
                    ...history,
                    comicDetails: {
                        image,
                        title,
                        type,
                        genres,
                        status,
                        score
                    }
                };
            } catch (error) {
                console.error(`Failed to fetch details for comic_id ${history.comic_id}`, error);
                return {
                    ...history,
                    comicDetails: null
                };
            }
        })
    );

    return historyWithComicDetails
}

const findChapterHistoryByuserIdandComicIdDb = async (userId, searchData) => {
    const result = await prisma.history.findMany({
        where: {
            userId,
            komik_id: searchData.komikId
        }
    });

    return result
}

const findLatestChapterByUserIdandComicIdDb = async (userId, searchData) => {
    const result = await prisma.history.findFirst({
        where: {
            userId,
            komik_id: searchData.komikId
        },
        orderBy: {
            createdAt: 'desc'
        }
    });

    return result
}

const deleteHistoryByHistoryIdDb = async (comicId) => {
    const result = await prisma.history.deleteMany({
        where: { komik_id: comicId }
    })

    return result
}

const addHistoryByUserIdDb = (userId, HistoryData) => {
    const HistoryResult = prisma.History.create({
        data: {
            userId: userId,
            komik_id: HistoryData.komikId,
            chapter_id: HistoryData.chapterId
        }
    })
    return HistoryResult
}

export {
    findComicHistoryByuserIdDb,
    deleteHistoryByHistoryIdDb,
    addHistoryByUserIdDb,
    findChapterHistoryByuserIdandComicIdDb,
    findLatestChapterByUserIdandComicIdDb
}