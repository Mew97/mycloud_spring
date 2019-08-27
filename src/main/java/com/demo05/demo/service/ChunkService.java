package com.demo05.demo.service;

import com.demo05.demo.model.Chunk;

public interface ChunkService {

    //  保存分块
    //void saveChunk(Chunk chunk);
    // 信息保存在redis中
    void rSaveChunk(Chunk chunk);

    //  检查文件块是否存在
    //boolean checkChunk(String identifier, Integer chunkNumber);

    boolean rCheckChunk(String identifier, Integer chunkNumber);
}
