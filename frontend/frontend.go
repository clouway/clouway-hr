package main

import (
	"bytes"
	"io"
	"log"
	"net/http"
	"net/http/httptest"
	"net/http/httputil"
	"net/url"
)

func main() {
	fs := NewFrontend()
	http.Handle("/", fs)

	log.Println("Listening...")
	if err := http.ListenAndServe(":8090", nil); err != nil {
		log.Printf("Got error while starting: %v", err)
	}

}

func NewFrontend() *Frontend {
	u, err := url.Parse("http://localhost:8080/")
	if err != nil {
		log.Fatal(err)
	}

	b := httputil.NewSingleHostReverseProxy(u)

	return &Frontend{fs: http.FileServer(http.Dir("build")), backend: b}
}

type Frontend struct {
	fs      http.Handler
	backend http.Handler
}

func (f Frontend) ServeHTTP(rw http.ResponseWriter, req *http.Request) {
	dup := f.duplicateRequest(req)

	recorder := httptest.NewRecorder()
	f.fs.ServeHTTP(recorder, req)

	if recorder.Code == http.StatusNotFound {
		log.Printf("Backend URL: %s\n", req.URL.Path)
		f.backend.ServeHTTP(rw, dup.req)
		return
	}

	// we copy the original headers
	for k, v := range recorder.Header() {
		rw.Header()[k] = v
	}

	rw.Write(recorder.Body.Bytes())

}

// nopCloser an internal reader used for the creation
// of dummy requests
type nopCloser struct {
	io.Reader
}

func (nopCloser) Close() error { return nil }

type ClonedRequest struct {
	req     *http.Request
	content []byte
}

// duplicateRequest creates a new copy of the provided http.Request.
func (f Frontend) duplicateRequest(request *http.Request) *ClonedRequest {
	// TODO(mgenov): remove this garbage
	b1 := new(bytes.Buffer)
	io.Copy(b1, request.Body)

	defer request.Body.Close()
	req1 := &http.Request{
		Method:        request.Method,
		URL:           request.URL,
		Proto:         "HTTP/1.1",
		ProtoMajor:    1,
		ProtoMinor:    1,
		Header:        request.Header,
		Body:          nopCloser{b1},
		Host:          request.Host,
		ContentLength: request.ContentLength,
	}
	return &ClonedRequest{req1, b1.Bytes()}
}
