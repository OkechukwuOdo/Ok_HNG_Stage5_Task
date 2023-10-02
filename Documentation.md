# API Documentation
This documentation contains the necessary  information on how to use the API for uploading and retrieving transcribed videos.

## Base URL

The base URL for accessing the API is: https://amara-hngtask-chrome-extension.onrender.com/

## API Endpoints
These are the endpoints

### Upload a Video
To upload a video, make a POST request to the `/api/upload` endpoint. It takes in `enctype="multipart/form-data"` property in your form, and an input name of `video`.
```html
<form action="/profile" method="post" enctype="multipart/form-data">
  <input type="file" name="video" />
</form>
```
- **Endpoint:**  `/api/upload`
- **HTTP Method**: `POST`
- **Description:** Upload a video file to be transcribed.


**Response:**
Status: 200 OK

```json
{
    "success": true,
    "message": "Video uploaded successfully",
    "public_id": "public_id_of_uploaded_video",
    "video_url": "URL_of_uploaded_video"
}
```

## Get Transcribed Video
To retrieve a transcribed video, make a GET request to the `/api/:public_id` endpoint, replacing `:public_id` with the id of the required video. You will receive the transcribed video in an HTML embed code.
- **Endpoint:** `/api/:public_id`
- **HTTP Method:** `GET`
- **Description:** Retrieve the transcribed video for a given public ID.


**Response:**
Status: 200 OK

```json
{
    "success": true,
    "message": "Video retrieved successfully",
    "data": "HTML_embed_code_for_transcribed_video"
}
```

## Error Handling
If an error on the upload or retrieval process, you will receive an error response with a 500 Internal Server Error status code and an error message.





