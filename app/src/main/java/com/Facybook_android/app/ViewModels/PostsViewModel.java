    package com.Facybook_android.app.ViewModels;

    import android.text.format.DateUtils;

    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.ViewModel;

    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Repository.repositories.PostsRepository;

    import java.util.List;
    import java.util.Timer;
    import java.util.TimerTask;

    public class PostsViewModel extends ViewModel {

        private PostsRepository mRepository;
        private LiveData<List<Post>> posts;
        private Timer timer;

        public PostsViewModel() {
            mRepository = new PostsRepository();
            posts = mRepository.getAll();
            timer = new Timer();
        }

        public void startTimer() {
            if (timer == null) {
                timer = new Timer();  // Additional check to handle any unintentional null assignments
            }
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    updatePosts();
                }
            }, 0, 60000);  // Update every minute
        }

        private void updatePosts() {
            List<Post> currentPosts = posts.getValue();
            if (currentPosts != null) {
                for (Post post : currentPosts) {
                    updatePostTimePublished(post);
                }
            }
        }

        private void updatePostTimePublished(Post post) {
            long currentTime = System.currentTimeMillis();
            CharSequence timePassed = DateUtils.getRelativeTimeSpanString(post.getCreationTime(),
                    currentTime, DateUtils.SECOND_IN_MILLIS);
            post.setDate(timePassed.toString());
            mRepository.update(post);
        }

        public void stopTimer() {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }

        public LiveData<List<Post>> get() { return posts; }
        public void getPosts() { mRepository.getPosts(); }
        public void getUsersPosts(String id) { mRepository.getUsersPosts(id); }
        public void add(Post post, String id) { mRepository.add(post, id); };
        public void reload() { mRepository.reload(); }
        public void update(Post post) { mRepository.update(post); }
        public void delete(String publisher, int postId) {mRepository.delete(publisher, postId); }
        public Post getPost(int id) {
            return mRepository.getPost(id);
        }
    }
