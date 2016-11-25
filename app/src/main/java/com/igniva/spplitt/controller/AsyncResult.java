package com.igniva.spplitt.controller;

public interface AsyncResult<String>
{
    public void onTaskResponse(String result, int urlResponseNo);
}