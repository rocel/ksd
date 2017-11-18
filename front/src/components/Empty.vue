<template>
    <div class="empty">
        <h1>Welcome to KSD</h1>
    </div>
</template>

<script type = "text/javascript" >
export default {
  name: 'Empty'
}
</script>

<style>
.empty {
    position: absolute;
    top: 0;
    left: 300px;
    width: calc(100% - 300px);
    background-color: #ffffff;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}
h1 {
    color: #2c3e50;
}
</style>