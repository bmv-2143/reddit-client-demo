package com.example.finalattestationreddit.data.repositories

import com.example.finalattestationreddit.data.data_sources.RedditNetworkDataSource
import com.example.finalattestationreddit.data.model.dto.post.Post
import com.example.finalattestationreddit.data.model.dto.post.PostData
import com.example.finalattestationreddit.data.network.RedditService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class RedditRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var redditNetworkDataSource: RedditNetworkDataSource
    private val redditService = mockk<RedditService>(relaxed = true)

    private lateinit var redditRepository: RedditRepository
    private val tokenManager = mockk<TokenManager>(relaxed = true)
    private val pagerFactory = mockk<PagerFactory>(relaxed = true)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        redditNetworkDataSource = RedditNetworkDataSource(redditService)
        redditRepository = RedditRepository(redditNetworkDataSource, tokenManager, pagerFactory)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getFirstPost() returns first post from RedditService response`() = runTest {

        // arrange
        coEvery {
            redditService.redditApi.getPostsById(TEST_POST_1.name).data.children
        } returns TEST_POST_DATA_RESPONSE

        // act
        val post = redditRepository.getFirstPost(TEST_POST_1.name)

        // assert
        assertEquals(TEST_POST_1, post)
        assertNotEquals(TEST_POST_2, post)
    }

    companion object {

        private val TEST_POST_1 = Post(
            name = "test_post_name_1",
            title = "test_post_title",
            author = "test_post_author",
            createdUtc = 123456789L,
            numComments = 100,
            permalink = "test_post_permalink",
            thumbnail = "test_post_thumbnail",
            url = "test_post_url",
            preview = null,
            selfText = "test_post_self_text",
            score = 100,
            likedByUser = true,
            saved = true
        )

        private val TEST_POST_2 = TEST_POST_1.copy(name = "test_post_name_2")

        private val TEST_POST_DATA = PostData(
            data = TEST_POST_1
        )

        private val TEST_POST_DATA_RESPONSE = listOf(
            TEST_POST_DATA,
            TEST_POST_DATA.copy(data = TEST_POST_2),
        )
    }
}