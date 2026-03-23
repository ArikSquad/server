package net.swofty.anticheat.world;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerWorld {
    private static final int CHUNK_SIZE = 16;
    private static final int MIN_WORLD_Y = -64;
    private static final int MAX_WORLD_Y = 320;

    private final Map<ChunkCoordinate, Chunk> chunks;

    private final ExecutorService executorService;
    private final ReentrantLock lock;
    private final BlockingQueue<Runnable> taskQueue;

    public PlayerWorld() {
        this.chunks = new HashMap<>();
        this.executorService = Executors.newSingleThreadExecutor();
        this.lock = new ReentrantLock();
        this.taskQueue = new LinkedBlockingQueue<>();

        executorService.submit(this::processTaskQueue);
    }

    private void processTaskQueue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Runnable task = taskQueue.take();
                task.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public CompletableFuture<Void> updateBlock(int x, int y, int z, Block block) {
        return CompletableFuture.runAsync(() -> {
            if (isInvalidY(y)) {
                return;
            }

            lock.lock();
            try {
                ChunkCoordinate chunkCoord = new ChunkCoordinate(
                    Math.floorDiv(x, CHUNK_SIZE),
                    Math.floorDiv(z, CHUNK_SIZE)
                );
                Chunk chunk = chunks.computeIfAbsent(chunkCoord, _ -> new Chunk());
                chunk.setBlock(Math.floorMod(x, CHUNK_SIZE), y, Math.floorMod(z, CHUNK_SIZE), block);
            } finally {
                lock.unlock();
            }
        }, executorService);
    }

    public CompletableFuture<Block> getBlock(int x, int y, int z) {
        return CompletableFuture.supplyAsync(() -> {
            if (isInvalidY(y)) {
                return null;
            }

            lock.lock();
            try {
                ChunkCoordinate chunkCoord = new ChunkCoordinate(
                    Math.floorDiv(x, CHUNK_SIZE),
                    Math.floorDiv(z, CHUNK_SIZE)
                );
                Chunk chunk = chunks.get(chunkCoord);
                if (chunk == null) {
                    return null;
                }
                return chunk.getBlock(Math.floorMod(x, CHUNK_SIZE), y, Math.floorMod(z, CHUNK_SIZE));
            } finally {
                lock.unlock();
            }
        }, executorService);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    private boolean isInvalidY(int y) {
        return y < MIN_WORLD_Y || y > MAX_WORLD_Y;
    }

    private static class ChunkCoordinate {
        final int x;
        final int z;

        ChunkCoordinate(int x, int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChunkCoordinate that = (ChunkCoordinate) o;
            return x == that.x && z == that.z;
        }

        @Override
        public int hashCode() {
            return 31 * x + z;
        }
    }

    private static class Chunk {
        private final Block[][][] blocks;

        public Chunk() {
            this.blocks = new Block[CHUNK_SIZE][256][CHUNK_SIZE];
        }

        public void setBlock(int x, int y, int z, Block block) {
            blocks[x][y][z] = block;
        }

        public Block getBlock(int x, int y, int z) {
            return blocks[x][y][z];
        }
    }
}
