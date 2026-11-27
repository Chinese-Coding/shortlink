package dev.haotangyuan.shortlink.service

/**
 * URL 标题接口层
 *
 * @author haotangyuan
 */
interface UrlTitleService {

    /**
     * 根据 URL 获取标题
     *
     * @param url URL
     * @return 标题
     */
    fun getTitleByUrl(url: String?): String
}
