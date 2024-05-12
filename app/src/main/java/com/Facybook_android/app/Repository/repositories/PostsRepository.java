    package com.Facybook_android.app.Repository.repositories;

    import androidx.lifecycle.LiveData;
    import androidx.lifecycle.MutableLiveData;
    import androidx.room.Room;

    import com.Facybook_android.app.API.PostAPI;
    import com.Facybook_android.app.Context.MyApplication;
    import com.Facybook_android.app.Model.entities.Post;
    import com.Facybook_android.app.Repository.AppDB;
    import com.Facybook_android.app.Repository.interfaces.PostDao;

    import java.util.LinkedList;
    import java.util.List;

    public class PostsRepository {
        private PostDao dao;
        private PostListData postListData;
        private PostAPI api;
        public PostsRepository() {
            AppDB db = Room.databaseBuilder(MyApplication.context,
                            AppDB.class, "PostsDB")
                    .fallbackToDestructiveMigration()
                    .build();
            dao = db.postDao();
            postListData = new PostListData();
            api = new PostAPI(postListData, dao);
        }

        public void getUsersPosts(String id) {
            api.fetchUsersPosts(id);
        }

        public Post getPost(int id) {
            return dao.get(id);
        }

        class PostListData extends MutableLiveData<List<Post>> {
            public PostListData() {
                super();
                setValue(new LinkedList<>());
            }

            @Override
            protected void onActive() {
                super.onActive();
                new Thread(() -> {
                    postListData.postValue(dao.index());
                }).start();
            }

        }
        public LiveData<List<Post>> getAll() {
            return postListData;
        }
        public void add(final Post post, String id) { api.add(post, id);}
        public void delete (String publisher, int postId) { api.delete(publisher, postId); }
        public void reload() { api.get(); }
        public void reload(String id) { api.fetchUsersPosts(id); }
        public void update(Post post) { api.update(post); }
        public void clear() { api.clear(); }
    }