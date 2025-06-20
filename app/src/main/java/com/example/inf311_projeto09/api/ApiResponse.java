package com.example.inf311_projeto09.api;

record ApiResponse<T>(boolean success, T dados) {
}
